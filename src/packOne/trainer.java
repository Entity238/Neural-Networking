package packOne;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class trainer {
	private final double DD=.01;
	private String saveFile="";
	private String dataFile="";
	private network trainingNet;
	
	public trainer(network intTrainingNet,String intSaveFile, String intDataFile){
		saveFile=intSaveFile;
		trainingNet=intTrainingNet;
		dataFile=intDataFile;
	}
	
	public List<List<Double>> learn(double rate){
		
		List<Double> initWeights = trainingNet.getWeights();
		List<Double> initBiases = trainingNet.getBiases();
		
		List<Double> newWeights = new ArrayList<Double>();
		List<Double> newBiases = new ArrayList<Double>();
		
		for(Double weight : initWeights) {
			newWeights.add(weight);
		}
		for(Double bias : initBiases) {
			newBiases.add(bias);
		}
		int counter=0;
		for(Double weight : newWeights) {
			double firstCost=cost(newWeights,newBiases);
			newWeights.set(counter, weight+Math.random()*DD);
			double secondCost=cost(newWeights,newBiases);
			if(secondCost>firstCost) {
				newWeights.set(counter, weight-DD-DD);
				secondCost=cost(newWeights,newBiases);
				if(secondCost>firstCost) {
					newWeights.set(counter, weight);
				}
			}
			counter++;
		}
		counter=0;
		for(Double bias : newBiases) {
			double firstCost=cost(newWeights,newBiases);
			newBiases.set(counter, bias+Math.random()*DD);
			double secondCost=cost(newWeights,newBiases);
			if(secondCost>firstCost) {
				newBiases.set(counter, bias-DD-DD);
				secondCost=cost(newWeights,newBiases);
				if(secondCost>firstCost) {
					newBiases.set(counter, bias);
				}
			}
			counter++;
		}
		
		List<List<Double>> returnList = new ArrayList<List<Double>>();
		returnList.add(newWeights);
		returnList.add(newBiases);
		return returnList;
	}
	
	public double cost(List<Double> weights, List<Double> biases) {
		double costSum=0;
		network calcNet = new network(trainingNet.getInitLayers(),weights,biases);
		File data = new File(dataFile);
		Scanner myReader;
		try {
			int dataCounter=0;
			myReader = new Scanner(data);
			while(myReader.hasNextLine()&&dataCounter<50) {
				String[] dataLine = myReader.nextLine().split(",");
				List<Double> inputs = new ArrayList<Double>();
				for(int i=2;i<dataLine.length-20;i++) {
					if(i==4) {
						inputs.add(Double.parseDouble(dataLine[i])/100);
					}else if(i==5) {
						inputs.add(Double.parseDouble(dataLine[i])/1000);
					}else {
						inputs.add(Double.parseDouble(dataLine[i]));
					}
				}
				List<Double> outputs = calcNet.calculate(inputs);
				for(double output:outputs) {
					if(dataLine[1]=="B") {
						dataLine[1]="0";
					}else {
						dataLine[1]="1";
					}
					costSum+=Math.pow(output-Double.parseDouble(dataLine[0]),2);
				}
				dataCounter++;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return costSum/50;
	}
	
	public void train(double aim) {
		double currentError=cost(learn(DD).get(0),learn(DD).get(1));
		int counting=0;
		File myFile = new File(saveFile);
		try {
			FileWriter writer = new FileWriter(myFile);
			
			while(currentError>aim) {
				List<List<Double>> returnedList = learn(0.0000001);
				double nowCost = cost(returnedList.get(0),returnedList.get(1));
				if(nowCost<currentError) {
					writer.write(returnedList+"    "+nowCost+" \r\n");
					currentError=nowCost;
				}
				counting++;
				trainingNet = new network(trainingNet.getInitLayers(),returnedList.get(0),returnedList.get(1));
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
