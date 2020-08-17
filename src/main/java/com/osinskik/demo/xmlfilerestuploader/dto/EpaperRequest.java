package com.osinskik.demo.xmlfilerestuploader.dto;

import java.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@XmlType
@XmlRootElement(name = "epaperRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpaperRequest {

  @XmlTransient
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant uploadTime;

  @Id
  @XmlTransient
  private String fileName;

  @XmlElement
  private DeviceInfo deviceInfo;

}
