package com.nexign.babybilling.brtservice.repo.projection;

public interface CustomerDataProjection {

    String getMsisnd();

    String getTariff();

    Integer getYear();
    Integer getMonth();
    Integer getMinutes();
    Integer getMinutesOther();
    Integer getTarMin();
    Integer getTarMinOth();
}
