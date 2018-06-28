package testjvm.testclassloader;

public class Hot {
	Hot hot = null;
    public void setHot(Hot hot) {
		this.hot = hot;
	}
	public void hot(){
        System.out.println("version 1:"+this.getClass().getClassLoader());
    }
    
    
}