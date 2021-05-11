import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:phone_number_validation_app/country_codes_provider.dart';

final StateNotifierProvider<PhoneNumbersNotifier, List<PhoneNumberEntry>>
    phoneNumbersNotifierProvider = StateNotifierProvider((ref) {
  return PhoneNumbersNotifier();
});

class PhoneNumbersNotifier extends StateNotifier<List<PhoneNumberEntry>> {
  PhoneNumbersNotifier() : super([]);

  void add(PhoneNumberEntry phoneNumber) {
    state = [phoneNumber, ...state];
  }
}

class PhoneNumberValidationResult {
  PhoneNumberValidationResult(this.isValid, this.formatted);

  final bool isValid;
  final String formatted;
}

class PhoneNumberEntry {
  PhoneNumberEntry(
      this.countryCode, this.value, this.dateTime, this.validationResult);

  final CountryCode countryCode;
  final String value;
  final DateTime dateTime;
  final PhoneNumberValidationResult validationResult;
}
