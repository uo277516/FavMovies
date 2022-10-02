package es.uniovi.eii.favmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import es.uniovi.eii.favmovies.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        //Recepcion de datos, atrapamos el intent
        Intent intent = getIntent();
        //Abro en el identificador
        int posCategoria = intent.getIntExtra(MainActivity.POS_CATEGORIA_SELECCIONADA,0);
        Categoria categEntrada = null;

        //Si la posición era mayor que 0, significa que del
        //spinner era una categoría ya creada que debemos
        //editar.
        if (posCategoria>0)
            categEntrada = intent.getParcelableExtra(MainActivity.CATEGORIA_SELECCIONADA);

        //de creacion o de edicion??
        TextView textViewCrea = (TextView) findViewById(R.id.textViewCreacion);
        final EditText editNomCategoria = (EditText) findViewById(R.id.editTextNombreC);
        final EditText editDesCategoria = (EditText) findViewById(R.id.editTextDescripcionC);

        //Recuperamos referencia al boton
        Button btnOK = (Button) findViewById(R.id.buttonOK);
        Button btnCancel = (Button) findViewById(R.id.buttonCancel);

        //ponemos etiqueta dependiendo si hay que crear o modifcar
        if (posCategoria==0) {
            textViewCrea.setText("Creación de nueva categoría");
        } else {
            textViewCrea.setText("Modificación de categoría existente");
            editNomCategoria.setText(categEntrada.getNombre());
            editDesCategoria.setText(categEntrada.getDescripcion());
            //no dejamos cambiar el nombre de la categoria
            editNomCategoria.setEnabled(false);
        }

        //si pulsa el ok
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tengo que mandai la nueva categoria pa tras
                Categoria categSalida = new Categoria(editNomCategoria.getText().toString(),
                        editDesCategoria.getText().toString());
                Intent intentResultado = new Intent();
                intentResultado.putExtra(MainActivity.CATEGORIA_MODIFICADA, categSalida);

                setResult(RESULT_OK, intentResultado);
                finish();
            }
        });

        //para el de cancelar
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish(); //finish es desapilar la activiy
            }
        });

    }
}