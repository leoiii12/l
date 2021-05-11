import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:phone_number_validation_app/phone_numbers_provider.dart';

class HistoryPage extends HookWidget {
  const HistoryPage({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final phoneNumbers = useProvider(phoneNumbersNotifierProvider);

    final tiles = phoneNumbers.map(
      (e) => ListTile(
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              "${e.countryCode.dialCode} ${e.value}",
              style: TextStyle(fontSize: 18.0),
            ),
            Text(
              "${e.validationResult.isValid ? 'VALID' : 'INVALID'}",
              style: TextStyle(
                  fontSize: 18.0,
                  color:
                      e.validationResult.isValid ? Colors.green : Colors.red),
            ),
          ],
        ),
        subtitle: Text(
          '${e.dateTime}',
          style: TextStyle(fontSize: 12.0),
        ),
      ),
    );

    final divided =
        ListTile.divideTiles(tiles: tiles, context: context).toList();

    return Scaffold(
      appBar: AppBar(title: Text('History')),
      body: ListView(
        children: phoneNumbers.length == 0
            ? [
                ListTile(
                  title: Text('No History.'),
                )
              ]
            : divided,
      ),
    );
  }
}
