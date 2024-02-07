package application;
import java.sql.Date;

public class Details {
    long id;
	long income;
	String incomereason;
	long amountspent;
	String expensereason;
	String category;
    Date date;
	long Tincome;
	long Texpense;
static long objid =0;    
	
	public Details(long id,long incom,String inreason,long amountspent, String outreason, String category, Date date,long Tin,long Texp) {
		this.id=id;
		this.income=incom;
		this.incomereason=inreason;
		this.amountspent = amountspent;
		this.expensereason = outreason;
		this.category = category;
		this.date =date;
        this.Tincome=Tin;
        this.Texpense=Texp;
  }
	



	public long getIncome() {
		return income;
	}




	public void setIncome(long income) {
		this.income = income;
	}




	public String getIncomereason() {
		return incomereason;
	}




	public void setIncomereason(String incomereason) {
		this.incomereason = incomereason;
	}




	public long getAmountspent() {
		return amountspent;
	}




	public void setAmountspent(long amountspent) {
		this.amountspent = amountspent;
	}




	public String getExpensereason() {
		return expensereason;
	}




	public void setExpensereason(String expensereason) {
		this.expensereason = expensereason;
	}




	public String getCategory() {
		return category;
	}




	public void setCategory(String category) {
		this.category = category;
	}




	public Date getDate() {
		return date;
	}




	public void setDate(Date date) {
		this.date = date;
	}




	public long getTincome() {
		return Tincome;
	}




	public void setTincome(long tincome) {
		Tincome = tincome;
	}




	public long getTexpense() {
		return Texpense;
	}




	public void setTexpense(long texpense) {
		Texpense = texpense;
	}


	Details(){

	}

}
