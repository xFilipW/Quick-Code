package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;
import com.example.quickcode.loginRegister.User;
import com.google.firebase.database.DataSnapshot;

public class FirebasePhoneNumberValidator implements Validator {

    private final String phoneNumber;
    private final Iterable<DataSnapshot> dataSnapshotIterable;

    public FirebasePhoneNumberValidator(String phoneNumber, Iterable<DataSnapshot> dataSnapshotIterable) {
        this.phoneNumber = phoneNumber;
        this.dataSnapshotIterable = dataSnapshotIterable;
    }

    @Override
    public ValidatorResult validate() {
        for (DataSnapshot dataSnapshot1 : dataSnapshotIterable) {
            User user = dataSnapshot1.getValue(User.class);

            String regex = "\\s+";
            String replacement = "";

            String safeUserPhoneNumber = user.getPhoneNumber().replaceAll(regex, replacement);
            String safePhoneNumber = phoneNumber.replaceAll(regex, replacement);

            if (safeUserPhoneNumber.equals(safePhoneNumber)) {
                return new ValidatorResult.Error(
                        new DeferredText.Resource(R.string.sign_up_page_label_error_account_with_this_phone_number_already_exists),
                        ErrorType.PHONE_NUMBER_ALREADY_USED
                );
            }
        }

        return new ValidatorResult.Success();
    }

}