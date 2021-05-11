import 'dart:convert';

import 'package:flutter/services.dart' show rootBundle;
import 'package:hooks_riverpod/hooks_riverpod.dart';

final StateNotifierProvider<CountryCodesNotifier, List<CountryCode>>
    countryCodesNotifierProvider = StateNotifierProvider((ref) {
  var countryCodesNotifier = CountryCodesNotifier();
  countryCodesNotifier.load();

  return countryCodesNotifier;
});

class CountryCodesNotifier extends StateNotifier<List<CountryCode>> {
  CountryCodesNotifier() : super([]);

  void load() {
    rootBundle.loadString('assets/country_codes.json').then((countryCodesStr) {
      final List<dynamic> countryCodeMaps =
          json.decode(countryCodesStr);
      final countryCodes = countryCodeMaps
          .map((e) =>
              CountryCode(e["dialCode"], e["flag"], e["alpha2"], e["name"]))
          .toList();

      state = countryCodes;
    });
  }
}

class CountryCode {
  CountryCode(this.dialCode, this.flag, this.alpha2, this.name);

  final String dialCode;
  final String flag;
  final String alpha2;
  final String name;
}
