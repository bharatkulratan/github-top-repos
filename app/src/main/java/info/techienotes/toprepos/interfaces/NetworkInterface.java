package info.techienotes.toprepos.interfaces;

import java.util.Map;

import info.techienotes.toprepos.model.RepoItem;
import info.techienotes.toprepos.networking.API;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * Created by bharatkulratan
 */

public interface NetworkInterface {
    @Headers({
            "Accept:application/json",
            "Content-Type:application/json",
    })
    @GET(API.SEARCH_REPO)
    Call<RepoItem> fetchTrendingRepos(@QueryMap Map<String, String> params);

}
