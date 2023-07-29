package packOne;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class network {
	private String saveName="";
	private List<List<neuron>> layers = new ArrayList<List<neuron>>();
	public int numNeurons=0;
	public int numWeights=0;
	private List<Integer> intLayers = new ArrayList<Integer>();
	
	public network(List<Integer> initLayers) {
		//initialize all neurons in each layer
		for(int i=0;i<initLayers.size();i++) {
			List<neuron> subLevel = new ArrayList<neuron>();
			for(int j=0;j<initLayers.get(i);j++) {
				neuron newNeuron = new neuron(0);
				subLevel.add(newNeuron);
				numNeurons++;
			}
			layers.add(subLevel);
		}
		
		for(int i=0;i<layers.size()-1;i++) {
			//i is across layer numbers except the last
			for(int j=0;j<(layers.get(i)).size();j++) {
				//j is across the size of the current layer
				for(int k=0;k<(layers.get(i+1)).size();k++) {
					//k is across the next layer's size
					neuron thisNeuron = getNeuron(i, j);
					neuron childNeuron = getNeuron(i+1,k);
					thisNeuron.addChild(childNeuron, Math.random());
					numWeights++;
				}
			}
		}
		intLayers=initLayers;
	}//ends constructor
	
	public network(List<Integer> initLayers, List<Double> initWeights, List<Double> initBiases) {
		//initialize all neurons in each layer
		for(int i=0;i<initLayers.size();i++) {
			List<neuron> subLevel = new ArrayList<neuron>();
			for(int j=0;j<initLayers.get(i);j++) {
				neuron newNeuron = new neuron(initBiases.get(numNeurons));
				subLevel.add(newNeuron);
				numNeurons++;
			}
			layers.add(subLevel);
		}
		for(int i=0;i<layers.size()-1;i++) {
			//i is across layer numbers except the last
			for(int j=0;j<(layers.get(i)).size();j++) {
				//j is across the size of the current layer
				for(int k=0;k<(layers.get(i+1)).size();k++) {
					//k is across the next layer's size
					neuron thisNeuron = getNeuron(i, j);
					neuron childNeuron = getNeuron(i+1,k);
					thisNeuron.addChild(childNeuron, initWeights.get(numWeights));
					numWeights++;
				}
			}
		}
		intLayers=initLayers;
	}//ends constructor
	
	public List<Integer> getInitLayers(){
		return intLayers;
	}
	
	public List<Double> getWeights(){
		List<Double> returnWeights = new ArrayList<Double>();
		for(List<neuron> layer : layers) {
			for(neuron nowNeuron : layer) {
				for(Double weight:nowNeuron.getConnections()) {
					returnWeights.add(weight);
				}
			}
		}
		return returnWeights;
	}
	
	public List<Double> getBiases(){
		List<Double> returnBiases = new ArrayList<Double>();
		for(List<neuron> layer : layers) {
			for(neuron nowNeuron:layer) {
				returnBiases.add(nowNeuron.getBias());
			}
		}
		return returnBiases;
	}
	
	public neuron getNeuron(int a,int b) {
		return (neuron) (layers.get(a)).get(b);
	}
	
	public List<List<neuron>> getLayers() {
		return (List<List<neuron>>)layers;
	}
	
	public List<Double> calculate(List<Double> input) {
		int counter=0;
		for(neuron firstNeuron:(List<neuron>) layers.get(0)) {
			firstNeuron.setVal(input.get(counter));
			counter++;
		}
		counter=0;
		List<Double> output = new ArrayList<Double>();
		output.add((((List<neuron>)(layers.get(layers.size()-1))).get(0)).calcTree());
		for(int m=1;m<((List<neuron>)(layers.get(layers.size()-1))).size();m++) {
			output.add((((List<neuron>)(layers.get(layers.size()-1))).get(m)).calcVal());
		}
		
		return output;
	}
}
