package org.example.picturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureUploadRequest implements Serializable {
    /**
     * 图片ID，用于后续修改
     */
    private Long id;
    private static final long SerialVersionUID = 1L;
}
