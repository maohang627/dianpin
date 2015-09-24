package com.thestore.microstore.vo.proxy;

import java.io.Serializable;
import java.util.List;

public class MyyhdProvinceVo
  implements Serializable
{
  private static final long serialVersionUID = 508131998524782407L;
  private Long id;
  private String provinceName;
  private List<MyyhdCityVo> cityVoList;

  public MyyhdProvinceVo()
  {
    this.id = null;

    this.provinceName = null;

    this.cityVoList = null; }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProvinceName() {
    return this.provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  public List<MyyhdCityVo> getCityVoList() {
    return this.cityVoList;
  }

  public void setCityVoList(List<MyyhdCityVo> cityVoList) {
    this.cityVoList = cityVoList;
  }
}