package boimpl;

import java.util.List;

import hibernateSignUp.HireMeTileTilesCatalog;
import notification.SuccessFailureWarnig;
import replyclasses.HireMeTilesCatalogReply;
import replyclasses.TileUserRegistrationReply;
import request.FetchTilesRecordRequest;
import request.HireMeTilesCatalogRequest;
import request.TileUserRegistrationRequest;

public interface HireMeTenseFreeBO {
public SuccessFailureWarnig saveNewUser(TileUserRegistrationReply request);
public SuccessFailureWarnig updateExistingUser(TileUserRegistrationReply request);
public TileUserRegistrationReply getAllUser(TileUserRegistrationReply request);
public TileUserRegistrationReply getSpecificUser(TileUserRegistrationReply request);
public SuccessFailureWarnig saveTileCatalogHirem(List<HireMeTileTilesCatalog> request);
public List<HireMeTilesCatalogRequest> fetchTilesRecord(FetchTilesRecordRequest request);
}
