package leandro.sp.com.br.trabalhandocommapas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Context context;

    LatLng google = new LatLng(40.740637, -74.002039);

    //private FusedLocationProviderClient mFusedLocationClient;


    private final int PERMISSAO_LOCALIZACAO_E_INTERNET_CODE = 0;


    /* For Google Fused API */
    //private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;



    private Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        context = getApplicationContext();


        verificarPermissoes();

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);


        //Conectando ao Google Play Services
        //colocar no app build gradle a dependência implementation 'com.google.android.gms:play-services-location:11.8.0'
         //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        try {



        } catch (Exception e) {
            e.printStackTrace();
        }



    }






    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        /* original
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */



        mMap = googleMap;
        // Localização sede da Google
        //LatLng google = new LatLng(40.740637, -74.002039);
        // Configuração da câmera
        final CameraPosition position = new CameraPosition.Builder()
                .target( google )     //  Localização
                .bearing( 45 )        //  Rotação da câmera
                .tilt( 90 )             //  Ângulo em graus
                .zoom( 17 )           //  Zoom // a partir do 17 fica em 3d
                .build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition( position );
        mMap.animateCamera( update ); //uma animação ao movimentar a camera

        //LatLng sydney = new LatLng(-34, 151);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



        // Criando um objeto do tipo MarkerOptions
        final MarkerOptions markerOptions = new MarkerOptions();

        // Configurando as propriedades do marker
        markerOptions.position( google )	// Localização
                .title("Google Inc.")	// Título
                .snippet("Sede da Google"); // Descrição

        // Adicionando marcador ao mapa
        mMap.addMarker( markerOptions );




        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                Toast.makeText(MapsActivity.this, "Latitude: " + latitude + ", Longitude: "+ longitude, Toast.LENGTH_SHORT).show();


                // Criar marker no ponto onde foi clicado
                MarkerOptions options = new MarkerOptions();
                options.position( latLng );
                mMap.addMarker( options );

                // Configurando as propriedades da Linha - traçar uma linha entre dois markers - o anteriornmente criado e o novo criado ao clicar
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add( google );
                polylineOptions.add( latLng );
                polylineOptions.color( Color.BLUE );
                // Adiciona a linha no mapa
                mMap.addPolyline( polylineOptions );


            }
        });


    }



    private void verificarPermissoes(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int p_ACCESS_FINE_LOCATION = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int p_ACCESS_COARSE_LOCATION = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

            int p_INTERNET = checkSelfPermission(Manifest.permission.INTERNET);
            int p_ACCESS_NETWORK_STATE = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);


            if (p_ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED || p_ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED  ||
                p_INTERNET != PackageManager.PERMISSION_GRANTED || p_ACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED)   {



                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.INTERNET,
                                                Manifest.permission.ACCESS_NETWORK_STATE
                }, PERMISSAO_LOCALIZACAO_E_INTERNET_CODE);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_LOCALIZACAO_E_INTERNET_CODE) {

            for (int permissao : grantResults) {
                if (permissao != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permissões não concedidas!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }





}
