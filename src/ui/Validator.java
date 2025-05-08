package ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The {@code Validator} class provides utility methods for validating and
 * cleaning user input.
 * It allows input validation using custom predicates and conversion to specific
 * types.
 */
public class Validator {
  private static Scanner reader = new Scanner(System.in);

  /**
   * Prompts the user for input, validates it, and converts it to a specified
   * type.
   *
   * @param <R>          The return type of the converted input.
   * @param initMessage  The message displayed to prompt user input.
   * @param errorMessage The message displayed when validation fails.
   * @param validate     A predicate function to validate the input.
   * @param convert      A function to convert the input into the desired type.
   * @return The user input converted into type {@code R}.
   */
  public static <R> R cleanInput(
      String initMessage,
      String errorMessage,
      Predicate<String> validate,
      Function<String, R> convert) {
    boolean run = true;
    String input;

    do {
      System.out.print(initMessage + ": ");
      input = reader.nextLine();
      if (validate.test(input)) {
        run = false;
      } else {
        System.out.println(errorMessage);
      }
    } while (run);

    return convert.apply(input);
  }

  /**
   * Prompts the user for input and validates it without conversion.
   *
   * @param initMessage  The message displayed to prompt user input.
   * @param errorMessage The message displayed when validation fails.
   * @param validate     A predicate function to validate the input.
   * @return The validated user input as a {@code String}.
   */
  public static String cleanInput(
      String initMessage,
      String errorMessage,
      Predicate<String> validate) {
    boolean run = true;
    String input;

    do {
      System.out.print(initMessage + ": ");
      input = reader.nextLine();
      if (validate.test(input)) {
        run = false;
      } else {
        System.out.println(errorMessage);
      }
    } while (run);

    return input;
  }

  /**
   * Checks if a given string can be parsed into an integer.
   *
   * @param t The string to check.
   * @return {@code true} if the string is a valid integer, {@code false}
   *         otherwise.
   */
  public static boolean isIntegerParsable(String t) {
    try {
      Integer.parseInt(t);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Checks if a given string can be parsed into a double.
   *
   * @param t The string to check.
   * @return {@code true} if the string is a valid double, {@code false}
   *         otherwise.
   */
  public static boolean isDoubleParsable(String t) {
    try {
      Double.parseDouble(t);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Checks if a given string can be parsed into a {@code LocalDate}.
   *
   * @param t The string to check.
   * @return {@code true} if the string is a valid date,
   *         {@code false} otherwise.
   */
  public static boolean isLocalDateParsable(String t) {
    try {
      LocalDate.parse(t);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  /**
   * Checks if the asked string equals "si"
   * 
   * @param message The string to check
   * @return {@code true} if the string equals "si",
   *         {@code false} otherwise.
   */
  public static boolean askYesNo(String message) {
    System.out.print(message + ": ");
    return reader.nextLine().trim().equalsIgnoreCase("si");
  }
}
