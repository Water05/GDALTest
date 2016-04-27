package cn.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class PlantWritable implements WritableComparable<Object>  {
	public String plant;
	public String noPlant;

	public PlantWritable(String plant, String noPlant){
		this.noPlant = noPlant;
		this.plant = plant;
		
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		 this.plant = in.readUTF();
		 this.noPlant = in.readUTF();
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.plant);
		out.writeUTF(this.noPlant);
		
	}
	public String toString() {
		return "植被像元： " + this.plant + " 非植被像元： " + this.noPlant;
	}
	@Override
	public int compareTo(Object o) {
		PlantWritable other = (PlantWritable) o;
		int n = this.plant.compareTo(other.plant);
		if(n != 0){
			return n;
		}
		n = this.noPlant.compareTo(other.noPlant);
		return n;
	}
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PlantWritable) {
        	PlantWritable other = (PlantWritable) obj;
            return this.strEquals(this.plant, other.plant)
                    && this.strEquals(this.noPlant, other.noPlant);
        }
        return false;
    }
 
    /**
     * 重写 hashCode() 方法很重要，Hadoop 的 Partitioners 会用到这个方法
     */
    public int hashCode() {
        return 13 * (this.plant == null ? 0 : this.plant.hashCode())
                + 67 * (this.noPlant == null ? 0 : this.noPlant.hashCode());
    }
 
    public boolean strEquals(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
 
}
