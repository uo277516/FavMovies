package es.uniovi.eii.favmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.favmovies.connectionUtil.Conexion;
import es.uniovi.eii.favmovies.modelo.Categoria;
import es.uniovi.eii.favmovies.modelo.Pelicula;

public class MainActivity extends AppCompatActivity {

    //identificar de activity
    private static final int GESTION_CATEGORIA = 1;


    public static final String POS_CATEGORIA_SELECCIONADA = "pos_categoria_seleccionada";
    public static final String CATEGORIA_SELECCIONADA = "categoria_seleccionada";
    public static final String CATEGORIA_MODIFICADA = "categoria_modificada";


    //Modelo de datos
    private List<Categoria> listaCategorias;

    //Componentes
    private Spinner spinner;
    private EditText editTitulo;
    private EditText editContenido;
    private EditText editFecha;
    private EditText editDuracion;
    private boolean creandoCategoria;

    //Pelicula actual
    private Pelicula pelicula;


    //Esto se le llama cuando vuelve a esta
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==GESTION_CATEGORIA) {
            if (resultCode == RESULT_OK) {
                Categoria cateAux = data.getParcelableExtra(CATEGORIA_MODIFICADA);
                Log.d("FavMovies.MainActivity", cateAux.toString());

                if (creandoCategoria) {
                    listaCategorias.add(cateAux);
                    introListaSpinner(spinner, listaCategorias);
                } else {
                    for (Categoria c : listaCategorias) {
                        if (c.getNombre().equals(cateAux.getNombre())) {
                            c.setDescripcion(cateAux.getDescripcion());
                            Log.d("FavMovies.MainActivity", "Modificada la descripcion de: "+c.getNombre());
                            break;
                        }
                    }
                }
            } else if (resultCode==RESULT_CANCELED) {
                Log.d("FavMovies.MainActivity", "CategoriaActivity cancelada");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Mis películas favoritas");


        //inicializo el modelo de datos
        listaCategorias = new ArrayList<Categoria>();
        listaCategorias.add(new Categoria("Acción", "Películas de acción"));
        listaCategorias.add(new Categoria("Comedia", "Películas de comedia"));

        //inicializo el spinner
        //antes teniamos unas categorias metidas en el diseño y ahora en codigo
        spinner = (Spinner) findViewById(R.id.spinnerCategorias);
        introListaSpinner(spinner, listaCategorias);

        FloatingActionButton btnGuardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos())
                    Snackbar.make(findViewById(R.id.layoutPrincipal), R.string.msg_guardado, Snackbar.LENGTH_LONG).show();
            }       //else con "los campos deben estar rellenados"
        });

        ImageButton btnCategoria = findViewById(R.id.btnModCategoria);
        btnCategoria.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar msgCrear;
                Spinner spinner = findViewById(R.id.spinnerCategorias);
                if (spinner.getSelectedItemPosition()==0) {
                    msgCrear = Snackbar.make(findViewById(R.id.layoutPrincipal), R.string.msgCrearCategoria, Snackbar.LENGTH_LONG);
                } else {
                    msgCrear = Snackbar.make(findViewById(R.id.layoutPrincipal), R.string.msgModCategoria, Snackbar.LENGTH_LONG);
                }


                msgCrear.setAction(R.string.accept, new View.OnClickListener() {  @Override
                    public void onClick(View view) {
                        Snackbar.make(findViewById(R.id.layoutPrincipal), R.string.msgAceptarAccion, Snackbar.LENGTH_LONG).show();
                        modificarCategoria();
                    }
                });
                msgCrear.show();

            }
        });


    }

    //un intent es un objeto de mensajeria para solicitar una accion de otro componente
    //de una app. Pide "permiso" para lanzar (en este caso la actividty)
    private void modificarCategoria() {
        Intent categoriaIntent = new Intent(MainActivity.this, CategoriaActivity.class);
        //le coloco al intent los gatos que le boy a pasar a la otra actividy (a la de la categoria)
        //necestio esto de pos pporq si es "sin definir" no tengo q hacer esto
        categoriaIntent.putExtra(POS_CATEGORIA_SELECCIONADA, spinner.getSelectedItemPosition());
        creandoCategoria=true;

        if (spinner.getSelectedItemPosition()>0) {
            creandoCategoria=false;
            //asocia el segundo valor al primero
            categoriaIntent.putExtra(CATEGORIA_SELECCIONADA,
                    listaCategorias.get(spinner.getSelectedItemPosition()-1));
        }

        startActivityForResult(categoriaIntent, GESTION_CATEGORIA);

    }

    private boolean validarCampos() {
        //no puede dar guardar sin meter las cosas
        EditText titulo = findViewById(R.id.editTextTitle);
        EditText arg = findViewById(R.id.editTextArgument);
        EditText duracion = findViewById(R.id.editTextDuracion);
        EditText fecha = findViewById(R.id.editTextFecha);

        if (titulo.getText().length()==0 || arg.getText().length()==0
            || duracion.getText().length()==0 || fecha.getText().length()==0)
            return false;
        return true;
    }




    private void introListaSpinner(Spinner spinner, List<Categoria> categoriaList) {
        ArrayList<String> nombres = new ArrayList<String>();  //array solo con los nombres
        nombres.add("Sin definir");
        for (Categoria c: categoriaList) {
            nombres.add(c.getNombre());
        }

        //arrayadapter usando array de strings y layout x defecto el del spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombres);
        //especifico layout para cuando aparezca
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //aplico el adapter
        spinner.setAdapter(adapter);
    }


    //--------- el menu --------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.Compartir) {
            Log.d("Guardar peli", "Guardar peli");
            guardarPeli();
            Conexion conexion = new Conexion (getApplicationContext());
            if (conexion.CompruebaConexion()) {
                compartirPeli();
            } else {
                Toast.makeText(getApplicationContext(), "No se ha establecido conexión", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void compartirPeli() {
        Intent itSend = new Intent(Intent.ACTION_SEND);
        itSend.setType("text/plain");
        itSend.putExtra(Intent.EXTRA_SUBJECT, "Te comparto la siguiente" +
                "peli: " + pelicula.getTitulo());
        itSend.putExtra(Intent.EXTRA_TEXT, "Titulo: " + pelicula.getTitulo() +
                "\nArgumento: " + pelicula.getArgumento());

        Intent shareIntent = Intent.createChooser(itSend, null);
        startActivity(shareIntent);
    }

    private void guardarPeli() {
        if (validarCampos()) {
            pelicula = new Pelicula (editTitulo.getText().toString(), editContenido.getText().toString(),
                    listaCategorias.get(spinner.getSelectedItemPosition()), editDuracion.getText().toString(),
                    editFecha.getText().toString());
        }
    }
}