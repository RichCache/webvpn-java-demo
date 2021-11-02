package com.richctrl.webvpn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @ToString
public class ConnectorException extends  Exception{
    private String code;
    private String message;
}
