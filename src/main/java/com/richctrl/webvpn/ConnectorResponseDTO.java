package com.richctrl.webvpn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor
public class ConnectorResponseDTO {
    private String code;
    private String status;
    private Object data;
}
