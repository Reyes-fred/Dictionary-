package com.yey.diccionario;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends Activity implements View.OnClickListener
{
    Button insert,display,eliminar;
    EditText et;
    TextView d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insert=(Button)findViewById(R.id.insertar);
        display=(Button)findViewById(R.id.mostrar);
        eliminar=(Button)findViewById(R.id.eliminar);
        et=(EditText) findViewById(R.id.editText1);
        d=(TextView)findViewById(R.id.Display);

        insert.setOnClickListener(this);
        display.setOnClickListener(this);
        eliminar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId()){
            case R.id.insertar:
                if(!et.getText().toString().equals("")) {
                   if(buscar(et.getText().toString())==-1) {

                       UserDictionary.Words.addWord(this,et.getText().toString(),1,"", Locale.getDefault());
                       //Contexto   Palabra a agregar   Frecuencia  Diccionario
                       // UserDictionary.Words.addWord(this, et.getText().toString(), 1, UserDictionary.Words.LOCALE_TYPE_ALL);
                        Toast.makeText(getApplicationContext(), "Palabra Agregada ", Toast.LENGTH_SHORT).show();
                        et.setText("");
                    }
                    else{
                       Toast.makeText(getApplicationContext(), "Palabra Actualizada ", Toast.LENGTH_SHORT).show();
                       et.setText("");
                    }
                }
                else{
                Toast.makeText(getApplicationContext(), "Ingresa una palabra",Toast.LENGTH_SHORT).show();
            }

                break;
            case R.id.mostrar:
                Uri dic = UserDictionary.Words.CONTENT_URI;
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(dic,null, null, null, null);
                d.setText("Lista de Palabras:");
                while (cursor.moveToNext())
                {
                    String word = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.WORD));
                    int id = cursor.getInt(cursor.getColumnIndex(UserDictionary.Words._ID));
                    String app = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.APP_ID));
                    int frequency = cursor.getInt(cursor.getColumnIndex(UserDictionary.Words.FREQUENCY));
                    String locale = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.LOCALE));
                    d.append("\nPalabra: "+word+"  Id: "+id+"  AppID: "+app+"  Frecuencia: "+frequency+" Locale: "+locale+"\n");
                }
                break;
            case R.id.eliminar:
                if (!et.getText().toString().equals("")) {
                if (!TextUtils.isEmpty(et.getText().toString())) {
                getContentResolver().delete(UserDictionary.Words.CONTENT_URI,
                        UserDictionary.Words.WORD + "=?", new String[] { et.getText().toString() });
                 et.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Palabra no encontrada",Toast.LENGTH_SHORT).show();
                }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ingresa una palabra",Toast.LENGTH_SHORT).show();
                }
                break;

        }


    }

    public int buscar(String palabra){
        Uri dic = UserDictionary.Words.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(dic,null, null, null, null);
        while (cursor.moveToNext())
        {
            String word = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.WORD));
            if(word.equals(palabra)){
                int frecuencia = cursor.getInt(cursor.getColumnIndex(UserDictionary.Words.FREQUENCY));
                getContentResolver().delete(UserDictionary.Words.CONTENT_URI,
                        UserDictionary.Words.WORD + "=?", new String[] { palabra });
                UserDictionary.Words.addWord(this, palabra, (frecuencia+1), "",Locale.getDefault());

                return 1;
            }

        }
        return  -1;

    }
}