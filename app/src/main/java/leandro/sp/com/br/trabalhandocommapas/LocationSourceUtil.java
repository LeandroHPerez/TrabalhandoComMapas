package leandro.sp.com.br.trabalhandocommapas;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

public class LocationSourceUtil implements LocationSource {
    private OnLocationChangedListener listener;
    @Override
    public void activate(OnLocationChangedListener listener) { //Chamado quando o layer (bolinha azul) da localização do usuário é iniciado
        this.listener = listener;
    }
    @Override
    public void deactivate() { //Chamado quando o layer (bolinha azul) da localização do usuário é finalizado
        this.listener = null;
    }

    //Método público para atualizar a posição da layer (bolinha azul) da localização do usuário
    public void setLocation(Location location) {
        if(this.listener != null) {
            this.listener.onLocationChanged(location); //Recebeu uma nova location e notifica o OnLocationChangedListener / atualiza a posição
        }
    }

    //Método público para atualizar a posição da layer (bolinha azul) da localização do usuário
    public void setLocation(LatLng latLng) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        if(this.listener != null) {
            this.listener.onLocationChanged(location); //Recebeu uma nova location e notifica o OnLocationChangedListener / atualiza a posição
        }
    }
}

