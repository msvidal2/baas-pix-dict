package com.picpay.banking.pix.original.fallbacks;

public abstract class ClientFallback {

    private Throwable cause;

    public ClientFallback(Throwable cause) {
        this.cause = cause;
    }

//    protected JDClientException resolveException() {
//        if(cause instanceof FeignException) {
//            var feignException = (FeignException) cause;
//            var error = parseError(feignException.responseBody().get().array());
//
//            switch (HttpStatus.resolve(feignException.status())) {
//                case NOT_FOUND:
//                    return new NotFoundClientException("Not found", cause, error, NOT_FOUND);
//                case BAD_REQUEST:
//                    return new ClientException("Bad Request", cause, error, BAD_REQUEST);
//            }
//        }
//
//        return new ClientException(cause.getMessage(), cause, null, INTERNAL_SERVER_ERROR);
//    }
//
//    protected ClientErrorDTO parseError(byte[] bytes) {
//        if(bytes == null || bytes.length == 0) {
//            return null;
//        }
//
//        try {
//            return new ObjectMapper().readValue(bytes, ClientErrorDTO.class);
//        } catch (IOException ioException) {
//            throw new ClientException("An error has occurred when parse the feign content error", ioException, null, INTERNAL_SERVER_ERROR);
//        }
//    }

}
