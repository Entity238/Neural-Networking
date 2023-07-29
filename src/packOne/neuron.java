package packOne;

import java.util.ArrayList;
import java.util.List;


public class neuron {
	private double bias=0;
	private double val=0;
	private List<neuron> children = new ArrayList<neuron>();
	private List<neuron> parents = new ArrayList<neuron>();
	private List<Double> connections= new ArrayList<Double>();
	private int dimension = 1;
	private double alpha=0;
	
	public neuron() {
		
	}
	
	public neuron(double intBias) {
		bias=intBias;
	}
	
	public neuron(double intBias, int setDimension){
		bias=intBias;
		dimension=setDimension;
		
	}
	
	public neuron(int intBias) {
		bias=(double)intBias;
	}
	
	public void setBias(double newBias){
		bias=newBias;
	}
	
	public void setVal(double newVal) {
		val=newVal;
	}
	
	public List<Double> getConnections(){
		return connections;
	}
	
	public neuron getParent(int n) {
		return parents.get(n);
	}
	
	public neuron getChild(int n) {
		return children.get(n);
	}
	
	public void addParent(neuron parent,double connects) {
		parents.add(parent);
		if(!parent.hasChild(this)) {
			parent.addChild(this,connects);
		}
	}
	
	public void addChild(neuron child,double connects) {
		children.add(child);
		connections.add(connects);
		if(!child.hasParent(this)) {
			child.addParent(this,connects);
		}
	}
	
	public boolean hasChild(neuron child) {
		for(neuron childs:children) {
			if(childs==child) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasParent(neuron parent) {
		for(neuron parentList : parents) {
			if(parentList==parent) {
				return true;
			}
		}
		return false;
	}
	
	public double getBias() {
		return bias;
	}
	
	public double getVal() {
		return val;
	}
	
	public void setConnection(neuron child, double newConnect) {
		int counter=0;
		for(neuron childs : children) {
			if(childs==child) {
				connections.set(counter, newConnect);
			}
			counter++;
		}
	}
	
	
	
	public double getConnection(neuron child) {
		int counter=0;
		for(neuron childs : children) {
			if(childs==child) {
				return connections.get(counter);
			}
			counter++;
		}
		System.out.println("Connection does not exist!");
		return 0;
	}
	
	public List<neuron> getChildren(){
		return children;
	}
	
	public double calcVal() {
		double sum=0;
		for(neuron parentList : parents) {
			sum+=parentList.getVal()*parentList.getConnection(this)+bias;
		}
		setVal(sum);
		return sum;
	}
	public double calcTree() {
		if(parents.size()==0) {
			return val;
		}else {
			for(neuron parent : parents) {
				parent.calcTree();
			}
			return calcVal();
		}
	}
}
