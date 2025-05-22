package ui;

import java.util.Optional;

/**
 * The {@code Response} class represents a generic response structure
 * that can be used to encapsulate a result, a success status, and a message.
 *
 * @param <T> The type of the result contained in the response.
 */
public class Response<T> {
  private boolean success;
  private String message;
  private Optional<T> result;

  /**
   * Constructs a Response with the specified success status, message, and result.
   * 
   * @param success A boolean indicating whether the operation was successful or
   *                not.
   * @param message A message providing more information about the operation's
   *                result.
   * @param result  The result of the operation, which can be null.
   */
  public Response(boolean success, String message, T result) {
    this.success = success;
    this.message = message;
    this.result = Optional.ofNullable(result);
  }

  /**
   * Returns whether the operation was successful or not.
   * 
   * @return True if the operation was successful, otherwise false.
   */
  public boolean getSuccess() {
    return success;
  }

  /**
   * Sets the success status of the response.
   * 
   * @param success A boolean indicating the new success status.
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

  /**
   * Returns the message associated with the response.
   * 
   * @return The message explaining the result of the operation.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message of the response.
   * 
   * @param message A string containing the new message.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Returns the result of the operation, wrapped in an Optional.
   * 
   * @return An Optional containing the result of the operation, which may be
   *         empty.
   */
  public Optional<T> getResult() {
    return result;
  }

  /**
   * Sets the result of the operation.
   * 
   * @param result The result of the operation, which may be null.
   */
  public void setResult(T result) {
    this.result = Optional.ofNullable(result);
  }

  /**
   * Creates a successful response with a message and result.
   * 
   * @param <T>     The type of the result.
   * @param message The message associated with the success.
   * @param result  The result of the operation.
   * @return A new successful Response.
   */
  public static <T> Response<T> success(String message, T result) {
    return new Response<>(true, message, result);
  }

  /**
   * Creates a successful response with a result and a default success message.
   * 
   * @param <T>    The type of the result.
   * @param result The result of the operation.
   * @return A new successful Response with a default success message.
   */
  public static <T> Response<T> success(T result) {
    return new Response<>(true, "Success", result);
  }

  /**
   * Creates a successful response with a message and no result.
   * 
   * @param <T>     The type of the result.
   * @param message The message associated with the success.
   * @return A new successful Response with a message and no result.
   */
  public static <T> Response<T> success(String message) {
    return new Response<>(true, message, null);
  }

  /**
   * Creates a failed response with a message and no result.
   * 
   * @param <T>     The type of the result.
   * @param message The message explaining the failure.
   * @return A new failed Response.
   */
  public static <T> Response<T> failure(String message) {
    return new Response<>(false, message, null);
  }
}
