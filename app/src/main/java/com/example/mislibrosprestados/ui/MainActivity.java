package com.example.mislibrosprestados.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.mislibrosprestados.R;
import com.example.mislibrosprestados.core.SqlIO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializar atbs
        this.sqlIO = new SqlIO(this.getApplicationContext());

        //inicializar vistas
        final ListView LV_LIBROS = this.findViewById(R.id.lvLibros);
        final Button BT_INSERTA = this.findViewById(R.id.btInserta);
        final Button BT_VER = this.findViewById(R.id.btVer);

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserta();
            }
        });


        BT_VER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ver();
            }
        });


        this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.entrada_libro_layout,
                null,
                new String[]{SqlIO.CAMPO_LIBRO_TITULO, SqlIO.CAMPO_LIBRO_DIAS_PRESTAMO},
                new int[]{R.id.lblTitulo, R.id.lblDias},
                0);

        LV_LIBROS.setAdapter(cursorAdapter);
    }

    private void inserta() {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        final EditText titulo = new EditText(this);

        dlg.setTitle("Nuevo titulo");
        dlg.setView(titulo);
        dlg.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqlIO.inserta(titulo.getText().toString());
                actualiza();
            }
        });
        dlg.create().show();

    }

    public void actualiza() {
        this.cursorAdapter.swapCursor(this.sqlIO.getCurosTodosLibros());
    }

    private void ver() {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        final StringBuilder toret = new StringBuilder();
        final Cursor CURSOR_LIBROS = this.sqlIO.getCurosTodosLibros();
        final int index = CURSOR_LIBROS.getColumnIndexOrThrow(sqlIO.CAMPO_LIBRO_TITULO);

        CURSOR_LIBROS.getCount();

        if (CURSOR_LIBROS.moveToFirst()) {
            do {
                toret.append(CURSOR_LIBROS.getString(index));
                toret.append("\n");
            } while (CURSOR_LIBROS.moveToNext());
        }

        dlg.setTitle("Libros");
        dlg.setMessage(toret.toString());
        dlg.create().show();

    }

    private SimpleCursorAdapter cursorAdapter;
    private SqlIO sqlIO;
}