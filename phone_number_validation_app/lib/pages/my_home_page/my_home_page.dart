import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:phone_number_validation_app/country_codes_provider.dart';
import 'package:phone_number_validation_app/pages/history_page/history_page.dart';
import 'package:phone_number_validation_app/pages/my_home_page/my_home_page_state_provider.dart';
import 'package:phone_number_validation_app/phone_numbers_provider.dart';

class MyHomePage extends HookWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  Widget build(BuildContext context) {
    final myHomePageState = useProvider(myHomePageStateNotifierProvider);

    final scrollController = ScrollController();

    return Scaffold(
      appBar: AppBar(
        title: Text(title),
        actions: [
          IconButton(
            icon: Icon(Icons.list),
            onPressed: () {
              Navigator.of(context).push(
                  MaterialPageRoute<void>(builder: (BuildContext context) {
                return HistoryPage();
              }));
            },
          )
        ],
      ),
      body: SingleChildScrollView(
        controller: scrollController,
        padding:
            EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
        child: Padding(
          padding: EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              SizedBox(height: 36),
              Image.asset('assets/images/logo_transparent.png'),
              SizedBox(height: 36),

              // Country Code
              Text(
                'Country Code',
              ),
              MyAutocompleteComponent(scrollController: scrollController),
              SizedBox(height: 24),

              // Phone Number
              Text(
                'Phone Number',
              ),
              Focus(
                child: TextField(
                  keyboardType: TextInputType.number,
                  onChanged: (str) {
                    context
                        .read(myHomePageStateNotifierProvider.notifier)
                        .selectPhoneNumber(str);
                  },
                  onSubmitted: (str) async {
                    await _onSubmit(context);
                  },
                ),
                onFocusChange: (hasFocus) async {
                  if (hasFocus) {
                    scrollController.animateTo(
                      320,
                      duration: Duration(milliseconds: 100),
                      curve: Curves.ease,
                    );
                  }
                },
              ),

              // Validations
              SizedBox(height: 24),
              myHomePageState.lastValidationResult == null
                  ? SizedBox(height: 0)
                  : Text(
                      'For ${myHomePageState.lastCountryCode.dialCode} ${myHomePageState.lastPhoneNumber}, it is',
                    ),
              myHomePageState.lastValidationResult == null
                  ? SizedBox(height: 0)
                  : Text(
                      '${myHomePageState.lastValidationResult.isValid ? 'Valid' : 'Invalid'}',
                      style: TextStyle(
                        color: myHomePageState.lastValidationResult.isValid
                            ? Colors.green
                            : Colors.red,
                        fontSize: 32,
                      ),
                    ),
              TextButton(
                onPressed: myHomePageState.isLoading == true
                    ? null
                    : () async {
                        await _onSubmit(context);
                      },
                child: Text(
                  myHomePageState.lastValidationResult == null
                      ? "Validate"
                      : "Validate Again",
                ),
              )
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _onSubmit(BuildContext context) async {
    FocusScope.of(context).unfocus();

    context
        .read(myHomePageStateNotifierProvider.notifier)
        .selectIsLoading(true);

    final overlayEntry = OverlayEntry(
      builder: (BuildContext context) {
        return IgnorePointer(
          child: BackdropFilter(
            filter: ImageFilter.blur(
              sigmaX: 5.0,
              sigmaY: 5.0,
            ),
            child: Center(
              child: SpinKitRotatingCircle(
                color: Colors.deepPurpleAccent,
                size: 50.0,
              ),
            ),
          ),
        );
      },
    );

    try {
      Overlay.of(context).insert(overlayEntry);

      var tuple3 = await context
          .read(myHomePageStateNotifierProvider.notifier)
          .getPhoneNumberValidationResult();
      var countryCode = tuple3.item1;
      var phoneNumber = tuple3.item2;
      var phoneNumberValidationResult = tuple3.item3;

      if (!phoneNumberValidationResult.isValid) {
        showDialog<void>(
          context: context,
          builder: (BuildContext context) {
            return AlertDialog(
              title: Text('Alert'),
              content: SingleChildScrollView(
                child: ListBody(
                  children: <Widget>[
                    Text('The phone number is invalid. Please input again.'),
                  ],
                ),
              ),
              actions: <Widget>[
                TextButton(
                  child: Text('OK'),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),
              ],
            );
          },
        );
      }

      var phoneNumberEntry = PhoneNumberEntry(
        countryCode,
        phoneNumber,
        DateTime.now(),
        phoneNumberValidationResult,
      );
      context.read(phoneNumbersNotifierProvider.notifier).add(phoneNumberEntry);
    } finally {
      overlayEntry.remove();

      context
          .read(myHomePageStateNotifierProvider.notifier)
          .selectIsLoading(false);
    }
  }
}

class MyAutocompleteComponent extends HookWidget {
  const MyAutocompleteComponent({
    Key key,
    @required this.scrollController,
  }) : super(key: key);

  final ScrollController scrollController;

  @override
  Widget build(BuildContext context) {
    final countryCodes = useProvider(countryCodesNotifierProvider);
    final myHomePageState = useProvider(myHomePageStateNotifierProvider);

    return Autocomplete<CountryCode>(
      optionsBuilder: (TextEditingValue textEditingValue) {
        if (textEditingValue.text == '' || textEditingValue.text == '+852') {
          return countryCodes;
        }

        return countryCodes.where((CountryCode countryCode) {
          return countryCode.dialCode
              .contains(textEditingValue.text.toLowerCase());
        });
      },
      displayStringForOption: (CountryCode countryCode) {
        return countryCode.dialCode;
      },
      fieldViewBuilder: (
        BuildContext context,
        TextEditingController textEditingController,
        FocusNode focusNode,
        VoidCallback onFieldSubmitted,
      ) {
        textEditingController.text = myHomePageState.countryCode.dialCode;
        textEditingController.selection = TextSelection.fromPosition(
            TextPosition(offset: textEditingController.text.length));

        return MyAutocompleteField(
          focusNode: focusNode,
          textEditingController: textEditingController,
          onFieldSubmitted: onFieldSubmitted,
          scrollController: scrollController,
        );
      },
      onSelected: (CountryCode selectedCountryCode) {
        context
            .read(myHomePageStateNotifierProvider.notifier)
            .selectCountryCode(selectedCountryCode);
      },
      optionsViewBuilder: (BuildContext context,
          AutocompleteOnSelected<CountryCode> onSelected,
          Iterable<CountryCode> options) {
        return MyAutocompleteOptions(
          onSelected: onSelected,
          options: options,
        );
      },
    );
  }
}

class MyAutocompleteField extends StatelessWidget {
  const MyAutocompleteField({
    Key key,
    @required this.focusNode,
    @required this.textEditingController,
    @required this.onFieldSubmitted,
    @required this.scrollController,
  }) : super(key: key);

  final FocusNode focusNode;

  final VoidCallback onFieldSubmitted;

  final TextEditingController textEditingController;

  final ScrollController scrollController;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: textEditingController,
      focusNode: focusNode,
      onFieldSubmitted: (String value) {
        onFieldSubmitted();
      },

      // Modified
      keyboardType: TextInputType.number,
      onTap: () {
        scrollController.animateTo(
          300,
          duration: Duration(milliseconds: 100),
          curve: Curves.ease,
        );
      },
    );
  }
}

class MyAutocompleteOptions extends StatelessWidget {
  const MyAutocompleteOptions({
    Key key,
    @required this.onSelected,
    @required this.options,
  }) : super(key: key);

  final AutocompleteOnSelected<CountryCode> onSelected;

  final Iterable<CountryCode> options;

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: Alignment.topLeft,
      child: Material(
        elevation: 4.0,
        child: Container(
          height: 200.0,

          // Modified
          width: MediaQuery.of(context).size.width - 32,
          child: ListView.builder(
            padding: EdgeInsets.zero,
            itemCount: options.length,
            itemBuilder: (BuildContext context, int index) {
              final CountryCode option = options.elementAt(index);
              return InkWell(
                onTap: () {
                  onSelected(option);
                },
                child: Padding(
                  padding: const EdgeInsets.all(16.0),

                  // Modified
                  child: Text("${option.flag}     ${option.dialCode}"),
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}
