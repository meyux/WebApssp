/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meyux.apssp.bean;

import com.meyux.apssp.util.DeviceInput;

import javax.annotation.PreDestroy;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

/**
 *
 * @author meyux
 */
@ManagedBean(name = CameraBean.BEAN_NAME)
@SessionScoped
public class CameraBean implements Serializable {

    private static final Logger logger =
            Logger.getLogger(CameraBean.class.toString());

    public static final String BEAN_NAME = "cameraBean";

    public static final String FILE_KEY = "file";
    public static final String IMAGE_HEIGHT_KEY = "intHeight";
    public static final String IMAGE_WIDTH_KEY = "intWidth";
    public static final String RELATIVE_PATH_KEY = "relativePath";
    public static final String CONTENT_TYPE_KEY = "contentType";

    private Map cameraImage = new HashMap();
    private File cameraFile;
    // uploaded video will be stored as a resource.
    private Resource outputResource;

    // upload error message
    private String uploadMessage;

    private Integer maxHeight;
    private Integer maxWidth;

    private boolean useImages = false;

    public CameraBean() {
        //super(CameraBean.class);
    }

    public void processUploadedImage(ActionEvent event) {
        if (cameraImage != null &&
                cameraImage.get("contentType") != null &&
                ((String)cameraImage.get("contentType")).startsWith("image")) {
            // clean up previously upload file
            if (cameraFile != null){
                disposeResources();
            }
            cameraFile = (File)cameraImage.get(FILE_KEY);
            if (cameraFile != null) {
                // copy the bytes into the resource object.
                try {
                    outputResource = DeviceInput.createResourceObject(
                        cameraFile, UUID.randomUUID().toString(),
                        (String) cameraImage.get(CONTENT_TYPE_KEY));
                } catch (IOException ex) {
                    logger.warning("Error setting up video resource object");
                }
                uploadMessage = "Upload was successful";
                return;
            }
        }else{
            // create error message for users.
            uploadMessage = "The uploaded image file could not be correctly processed.";
        }
        // a null/empty object is used in the page to hide the audio
        // component.
        outputResource = null;
    }

    public void reset(ActionEvent event) {
		cameraImage = new HashMap();
		disposeResources();
		outputResource = null;
		uploadMessage = "";
	}

    @PreDestroy
    public void disposeResources(){
        if( cameraFile != null ){
            boolean success = cameraFile.delete();
            if (!success && logger.isLoggable(Level.FINE)){
                logger.fine("Could not dispose of media file" + cameraFile.getAbsolutePath());
            }
        }
    }

    public void setCameraImage(Map cameraImage) {

        this.cameraImage = cameraImage;

    }

    public Map getCameraImage() {
        return cameraImage;
    }

    public Resource getOutputResource() {
        return outputResource;
    }

    public String getUploadMessage() {
        return uploadMessage;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer height) {
        this.maxHeight = height;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer width) {
        this.maxWidth = width;
    }

    public boolean isUseImages() {
        return useImages;
    }

    public void setUseImages(boolean useImages) {
        this.useImages = useImages;
    }
}
