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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;



//url1: https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/unit-4-add-geo-features-to-your-apps/lesson-7-location/7-1-p-use-the-device-location/7-1-p-use-the-device-location.html
//url2: https://androidclarified.com/fusedlocationproviderclient-current-location-example/



//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Context context;

    LatLng google = new LatLng(40.740637, -74.002039);

    LatLng latLngAvPaulista = new LatLng(-23.564224, -46.653156);


    LatLng latLngRuaJoseParonetto =  new LatLng(-21.784125, -48.14906);

    LatLng latLngRuaJoseParonetto2 = new LatLng(-21.783649, -48.148783);

    //private FusedLocationProviderClient mFusedLocationClient;


    private final int PERMISSAO_LOCALIZACAO_E_INTERNET_CODE = 0;


    /* For Google Fused API */
    //private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;



    private Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;


    private GoogleApiClient googleApiClient;

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


        /*
        //Instantiating the GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

                */

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

        //Ajustar o tipo de mapa  - (GoogleMap.MAP_TYPE_NORMAL, MAP_TYPE_HYBRID, MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN, MAP_TYPE_NONE
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Exibe a localização do usuário - antes de ser chamado exige a permissão ACCESS_FINE_LOCATION (GPS)
                mMap.setMyLocationEnabled(true);
            }
            else {
                Toast.makeText(MapsActivity.this, "Você deve aceitar as permissões necessárias de localização para poder utilizar este aplicativo", Toast.LENGTH_SHORT).show();
                finish(); //sai do app
            }
        } else {
            //Fora da api-23, não é necessário verificar em runtime a permissão
            mMap.setMyLocationEnabled(true);
        }




        // Configuração da câmera com animação
        final CameraPosition position = new CameraPosition.Builder()
                .target( latLngRuaJoseParonetto )     //  Localização
                .bearing( 45 )                        //  Rotação da câmera em graus
                .tilt( 90 )                           //  Ângulo que a câmera está posicionada em graus
                .zoom( 17 )                           //  Zoom // a partir do 17 fica em 3d
                .build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition( position );
        //mMap.moveCamera(update); //movimenta a câmera bruscamente, sem animação
        mMap.animateCamera( update ); //uma animação ao movimentar a camera




        //Marcadores adicionados ao mapa
        adicionarMarcador(mMap, google, "Google Inc.", "Sede da Google", null);

        //Marcador com imagem padrão
        adicionarMarcador(mMap, latLngRuaJoseParonetto, "Ponto Adicionado", "Ponto de teste", null);

        //Marcador com imagem customizada
        adicionarMarcador(mMap, latLngRuaJoseParonetto2, "Ponto Adicionado com imagem", "Ponto de teste imagem", R.mipmap.ic_launcher);
        // Fim Marcadores adicionados ao mapa



        //Listener para cliques no mapa
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
                polylineOptions.add( latLngRuaJoseParonetto );
                polylineOptions.add( latLng );
                polylineOptions.color( Color.BLUE );
                // Adiciona a linha no mapa
                mMap.addPolyline( polylineOptions );

                //Para mover a câmera para o ponto de clique:
                CameraUpdate update = CameraUpdateFactory.newLatLng( latLng );
                mMap.animateCamera(update);


            }
        });



        //Listener para movimento de câmera, executado quando o movimento termina
        //Atualmente existem três listeners de câmera:
        // 1 - GoogleMap.OnCameraMoveStartedListener,
        // 2 - GoogleMap.OnCameraMoveListener,
        // 3 - GoogleMap.OnCameraIdleListener
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng mPosition = mMap.getCameraPosition().target;
                float mZoom = mMap.getCameraPosition().zoom;

                double latitude =  mMap.getCameraPosition().target.latitude;
                double longitude =  mMap.getCameraPosition().target.longitude;

                Toast.makeText(MapsActivity.this, "OnCameraIdleListener() -> Latitude: " + latitude + ", Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
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


    private void adicionarMarcador(GoogleMap mMap, LatLng latLng, String titulo, String snippet, Integer idRecursoImagem ) {

        MarkerOptions markerOptions = new MarkerOptions();
        // Configurando as propriedades do marker
        markerOptions.position( latLng )	// Localização
                .title(titulo)	    // Título
                .snippet(snippet); // Descrição
        //Customiza o ícone do marcador
        if(idRecursoImagem != null){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(idRecursoImagem));
        }

        //Adiciona o marcador no mapa
        mMap.addMarker(markerOptions);

    }


}
