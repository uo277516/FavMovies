package es.uniovi.eii.favmovies.connectionUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Conexion {

    private Context mContexto;

    //Conexto de la conexion
    public Conexion(Context mContexto) {
        this.mContexto=mContexto;
    }

    public boolean CompruebaConexion() {
        boolean conectado = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        conectado = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return conectado;
    }

}
