package edu.handong.csee.pp1.dp.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.handong.csee.pp1.dp.RawInstance;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class WekaUtils {
	
	/**
	 * Write an arff file with instances
	 * @param instances Instances object
	 * @param targetFileName An arff file is saved with this parameter value
	 */
	static public void writeADataFile(Instances instances,String targetFileName){
		try {
			File file= new File(targetFileName);
			if(file.exists()){
				return;
			}

			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos=new DataOutputStream(fos);

			dos.write((instances.toString()).getBytes());

			dos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("FileName: " + targetFileName);
			System.exit(0);
		} 
	}
	
	static public Instances generateWekaInstances(ArrayList<RawInstance> rawInstances) {
		
		ArrayList<Attribute> attributes = createAttributeInfoForClassfication(RawInstance.NUMMETRICS + 1); //for label +1
		Instances newInstances = new Instances("newData", attributes, 0);
		
		for(RawInstance rawInstance:rawInstances) {
			double[] values = new double[attributes.size()];
			
			for(int i=0; i < attributes.size()-1; i++) {
				values[i] = rawInstance.getValues()[i];
			}
			
			values[attributes.size()-1] = Double.NaN; // 메트릭만 수집해서 버그인지 아닌지 알 수 없어서, 레이블 정보는 그냥 NaN값 입력.
			
			newInstances.add(new DenseInstance(1.0, values));
		}
		
		newInstances.setClass(newInstances.attribute(labelName));
		
		return newInstances;
	}

	/**
	 * Create a list of attributes for the given number of attributes
	 * @param numOfAttributes The number of attributes to create
	 * @return ArrayList of Attribute
	 */
	static public ArrayList<Attribute> createAttributeInfoForClassfication(long numOfAttributes){
		// create attribute information
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		// add attributes from matchedAttribute
		for(int i=0; i<numOfAttributes-1;i++){
			Attribute attribute = new Attribute("attr" + (i+1));
			attributes.add(attribute);
		}

		//add label as the last attribute
		ArrayList<String> labels = new ArrayList<String>();
		labels.add(strPos);
		labels.add(strNeg);
		Attribute label = new Attribute(labelName, labels);
		attributes.add(label);

		dblPosValue = attributes.get(attributes.size()-1).indexOfValue(strPos);
		dblNegValue = attributes.get(attributes.size()-1).indexOfValue(strNeg);

		return attributes;
	}
	
	/** String value of the positive label */
	static final public String strPos = "buggy";
	/** String value of the negative label */
	static final public String strNeg = "clean";
	/** String value of label attribute */
	static final public String labelName = "label";
	/** double value of the positive label */
	static public double dblPosValue = 0;
	/** double value of the positive label */
	static public double dblNegValue = 1;
}
