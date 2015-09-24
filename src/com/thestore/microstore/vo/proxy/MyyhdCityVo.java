package com.thestore.microstore.vo.proxy;

import java.io.Serializable;
import java.util.List;

public class MyyhdCityVo
  implements Serializable
{
  private static final long serialVersionUID = 1718364969787740078L;
  private Long id;
  private String cityName;
  private List<MyyhdCountyVo> countyVoList;

  public MyyhdCityVo()
  {
    this.id = null;

    this.cityName = null;

    this.countyVoList = null; }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCityName() {
    return this.cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public List<MyyhdCountyVo> getCountyVoList() {
    return this.countyVoList;
  }

  public void setCountyVoList(List<MyyhdCountyVo> countyVoList) {
    this.countyVoList = countyVoList;
  }
}