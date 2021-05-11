import 'dart:convert';

import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:phone_number_validation_app/country_codes_provider.dart';
import 'package:tuple/tuple.dart';

import '../../phone_numbers_provider.dart';

final StateNotifierProvider<MyHomePageStateNotifier, MyHomePageState>
    myHomePageStateNotifierProvider =
    StateNotifierProvider((ref) => MyHomePageStateNotifier());

class MyHomePageStateNotifier extends StateNotifier<MyHomePageState> {
  MyHomePageStateNotifier() : super(_initialValue);

  static final _initialValue = MyHomePageState(
    CountryCode("+852", "🇭🇰", "HK", "Hong Kong"),
    "",
    null,
    null,
    null,
    false,
  );

  void selectCountryCode(CountryCode countryCode) {
    state = MyHomePageState(
      countryCode,
      state.phoneNumber,
      state.lastCountryCode,
      state.lastPhoneNumber,
      state.lastValidationResult,
      state.isLoading,
    );
  }

  void selectPhoneNumber(String phoneNumber) {
    state = MyHomePageState(
      state.countryCode,
      phoneNumber,
      state.lastCountryCode,
      state.lastPhoneNumber,
      state.lastValidationResult,
      state.isLoading,
    );
  }

  void selectIsLoading(bool isLoading) {
    state = MyHomePageState(
      state.countryCode,
      state.phoneNumber,
      state.lastCountryCode,
      state.lastPhoneNumber,
      state.lastValidationResult,
      isLoading,
    );
  }

  void selectLastValidationResult(
    CountryCode countryCode,
    String phoneNumber,
    PhoneNumberValidationResult phoneNumberValidationResult,
  ) {
    state = MyHomePageState(
      state.countryCode,
      state.phoneNumber,
      countryCode,
      phoneNumber,
      phoneNumberValidationResult,
      state.isLoading,
    );
  }

  Future<Tuple3<CountryCode, String, PhoneNumberValidationResult>>
      getPhoneNumberValidationResult() async {
    // This is my personal endpoint which includes the secret
    final url = Uri.parse('https://pnva-api.leochoi.workers.dev');
    final response = await http.post(url,
        body: jsonEncode(<String, String>{
          "phoneNumber":
              "${state.countryCode.dialCode}${state.phoneNumber.trim()}"
        }));

    PhoneNumberValidationResult phoneNumberValidationResult;
    if (response.statusCode == 200) {
      Map<String, dynamic> resMap = jsonDecode(response.body);

      phoneNumberValidationResult =
          PhoneNumberValidationResult(true, resMap['national_format']);
    } else {
      phoneNumberValidationResult =
          PhoneNumberValidationResult(false, state.phoneNumber.trim());
    }

    state = MyHomePageState(
      state.countryCode,
      state.phoneNumber,
      state.countryCode,
      state.phoneNumber,
      phoneNumberValidationResult,
      state.isLoading,
    );

    return Tuple3<CountryCode, String, PhoneNumberValidationResult>(
      state.countryCode,
      state.phoneNumber,
      phoneNumberValidationResult,
    );
  }
}

class MyHomePageState {
  const MyHomePageState(
    this.countryCode,
    this.phoneNumber,
    this.lastCountryCode,
    this.lastPhoneNumber,
    this.lastValidationResult,
    this.isLoading,
  );

  final CountryCode countryCode;
  final String phoneNumber;

  final CountryCode lastCountryCode;
  final String lastPhoneNumber;

  final PhoneNumberValidationResult lastValidationResult;

  final bool isLoading;
}
