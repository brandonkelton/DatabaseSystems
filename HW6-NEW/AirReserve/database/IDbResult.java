/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airreserve.database;

/**
 *
 * @author brand
 */
public interface IDbResult<T> {
    
    boolean getIsError();
    
    void setIsError(boolean isError);
    
    String getMessage();
    
    void setMessage(String message);
    
    T getResult();
    
    void setResult(T result);
}
