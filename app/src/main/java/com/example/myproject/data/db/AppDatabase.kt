package com.example.myproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myproject.data.model.Product
import com.example.myproject.data.dao.ProductDao
import com.example.myproject.data.dao.GroupDao
import com.example.myproject.data.model.Group

@Database(entities = [Product::class, Group::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun groupDao(): GroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE products 
                    ADD COLUMN group_id INTEGER 
                    REFERENCES groups(id) 
                    ON DELETE SET NULL
                """)
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создаем временную таблицу с новой структурой
                database.execSQL("""
                    CREATE TABLE groups_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        barcode TEXT
                    )
                """)
                
                // Копируем данные из старой таблицы в новую, преобразуя поля
                database.execSQL("""
                    INSERT INTO groups_new (id, name, barcode)
                    SELECT id, name, 
                        CASE 
                            WHEN barcode IS NULL OR barcode = '' THEN NULL 
                            ELSE barcode 
                        END
                    FROM groups
                """)
                
                // Удаляем старую таблицу
                database.execSQL("DROP TABLE groups")
                
                // Переименовываем новую таблицу
                database.execSQL("ALTER TABLE groups_new RENAME TO groups")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 