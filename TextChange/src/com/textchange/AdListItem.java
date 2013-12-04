package com.textchange;

public class AdListItem {

	public String bookTitle;
	public String bookAuthor;
	public String bookPrice;
	public String adId;
	public String getbookTitle(){
		return bookTitle;
	}
	
	public String getbookAuthor(){
		return bookAuthor;
	}
	
	public String getbookPrice(){
		return bookPrice;
	}
	
	public String getbookId(){
		return adId;
	}
	
	public void setbookTitle(String bookTitle) {
	        this.bookTitle = bookTitle;
	    }
	
	public void setbookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
	
	public void setbookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }
	public void setbookId(String adId){
		this.adId=adId;
	}
	
	public String toString(){
		 return "[ bookTitle=" + bookTitle + ", bookAuthor=" +
	                bookAuthor + " , Price=" + bookPrice + "]";
	}
		 
}
