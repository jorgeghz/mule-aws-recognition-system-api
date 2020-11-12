package com.emoran;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

public class ImageProcessor {
	
	public static Object getImageLabels(String base64Image,String accessToken,String accessSecret) throws FileNotFoundException, IOException {
		
		List <Label> labels =  new ArrayList<Label>();
		try {
			String base64Text = base64Image.replaceFirst("^.*,", "");
			
			Base64 b64 =new Base64();
			byte[] bytes = b64.decode(base64Text);
		    	
        	ByteBuffer imageBytes = ByteBuffer.wrap(bytes);    
        	
        	BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessToken, accessSecret);        	
        	AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2).withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
        	
        	DetectLabelsRequest request = new DetectLabelsRequest().withImage(new Image().withBytes(imageBytes)).withMaxLabels(10).withMinConfidence(77F);
        	
        	DetectLabelsResult result = rekognitionClient.detectLabels(request);
            labels = result.getLabels();

            System.out.println("Detected labels");

            for (Label label: labels) {
            	System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }

        } 
        catch (AmazonRekognitionException e) {
        	e.printStackTrace();
        }
		
		return labels;
	}
}
