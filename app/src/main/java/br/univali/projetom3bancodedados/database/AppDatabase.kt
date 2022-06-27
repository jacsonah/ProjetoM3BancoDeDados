package br.univali.projetom3bancodedados.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.univali.projetom3bancodedados.jigsawpuzzle.database.Piece
import br.univali.projetom3bancodedados.jigsawpuzzle.database.PieceDao
import br.univali.projetom3bancodedados.paint.database.DrawPath
import br.univali.projetom3bancodedados.paint.database.DrawPathDao

@Database(entities = [DrawPath::class, Piece::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    abstract fun drawPathDao(): DrawPathDao
    abstract fun pieceDao(): PieceDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
