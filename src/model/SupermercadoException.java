package model;

// Exceção do tipo unchecked herdando de RuntimeException
public class SupermercadoException extends RuntimeException {
    public SupermercadoException(String mensagem) {
        super(mensagem);
    }
}