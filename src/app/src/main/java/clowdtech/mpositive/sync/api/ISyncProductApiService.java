package clowdtech.mpositive.sync.api;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ISyncProductApiService {
    @POST("/account/{account}/newProducts")
    void createProducts(@Path("account") String account, @Body NewProductsRequest request, Callback<NewProductsResponse> response);

    @POST("/account/{account}/updateProducts")
    void updateProducts(@Path("account") String account, @Body UpdateProductsRequest request, Callback<UpdateProductsResponse> response);
}


