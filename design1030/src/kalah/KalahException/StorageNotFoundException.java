package kalah.KalahException;
public class StorageNotFoundException extends Exception {
    //异常信息：没有找到的存储信息
    public StorageNotFoundException(String message) {
        super(message);
    }
}
