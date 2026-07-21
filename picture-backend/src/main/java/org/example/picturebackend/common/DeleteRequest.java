package org.example.picturebackend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    //id
    private Long Id;

    private static final long serialVersionUID = 1L;
}
