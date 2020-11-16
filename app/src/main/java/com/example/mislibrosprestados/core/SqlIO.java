package com.example.mislibrosprestados.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class SqlIO extends SQLiteOpenHelper {

    private static final String BD_NOMBE = "librosPrestados";
    private static final int BD_VERSION = 1;

    public static final String TABLA_LIBROS = "libros";
    public static final String CAMPO_LIBROS_ID = "_id";
    public static final String CAMPO_LIBRO_TITULO = "titulo";
    public static final String CAMPO_LIBRO_AUTOR = "autor";
    public static final String CAMPO_LIBRO_FECHA_PRESTAMO = "fecha_prestamo";
    public static final String CAMPO_LIBRO_DIAS_PRESTAMO = "dias_prestamo";

    public SqlIO(@Nullable Context context) {
        super(context, BD_NOMBE, null, BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(BD_NOMBE, "CREANDO TABLAS");
            db.beginTransaction();

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_LIBROS + "(" + CAMPO_LIBROS_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CAMPO_LIBRO_TITULO + " TEXT NOT NULL,"
                    + CAMPO_LIBRO_AUTOR + " TEXT,"
                    + CAMPO_LIBRO_FECHA_PRESTAMO + " INTEGER NOT NULL ,"
                    + CAMPO_LIBRO_DIAS_PRESTAMO + " INTEGER NOT NULL"
                    + ")");

            db.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(BD_NOMBE, error.getMessage());
        } finally {
            db.endTransaction();
        }

        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i(BD_NOMBE, "CREANDO TABLAS");
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_LIBROS);
            db.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(BD_NOMBE, error.getMessage());
        } finally {
            db.endTransaction();
        }
        this.onCreate(db);
    }

    public Cursor getCurosTodosLibros() {
        final SQLiteDatabase BD = this.getReadableDatabase();

        return BD.query(TABLA_LIBROS, null, null, null, null, null, CAMPO_LIBRO_FECHA_PRESTAMO);
    }

    public void inserta(String titulo) {
        final SQLiteDatabase BD = this.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_LIBRO_TITULO, titulo);
        VALORES.put(CAMPO_LIBRO_DIAS_PRESTAMO, 15);
        VALORES.put(CAMPO_LIBRO_FECHA_PRESTAMO, Calendar.getInstance().getTimeInMillis());
        try {
            Log.i(BD_NOMBE, "INSERTANDO");
            BD.beginTransaction();
            BD.insert(TABLA_LIBROS, null, VALORES);

            BD.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(BD_NOMBE, error.getMessage());
        } finally {
            BD.endTransaction();
        }
    }
}
