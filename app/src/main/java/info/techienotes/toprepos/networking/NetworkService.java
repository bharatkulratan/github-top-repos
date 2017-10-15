package info.techienotes.toprepos.networking;

import java.util.Map;

import info.techienotes.toprepos.interfaces.NetworkInterface;
import info.techienotes.toprepos.model.RepoItem;
import retrofit2.Call;

/**
 * Created by bharatkulratan
 */

public class NetworkService {
    public static Call<RepoItem> fetchRepos(Map<String, String> params ) {

        NetworkInterface networkInterface =
                ServiceGenerator.createService(NetworkInterface.class);

        return networkInterface.fetchTrendingRepos(params);
    }
}
