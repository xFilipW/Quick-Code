package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;
import com.example.quickcode.loginRegister.User;
import com.google.firebase.database.DataSnapshot;

public class FirebaseEmailValidator implements Validator {

    private final String email;
    private final Iterable<DataSnapshot> dataSnapshotIterable;

    public FirebaseEmailValidator(String email, Iterable<DataSnapshot> dataSnapshotIterable) {
        this.email = email;
        this.dataSnapshotIterable = dataSnapshotIterable;
    }

    @Override
    public ValidatorResult validate() {
        for (DataSnapshot dataSnapshot1 : dataSnapshotIterable) {
            User user = dataSnapshot1.getValue(User.class);

            String userEmail = user.getEmail();
            String safeEmail = email;

            if (userEmail.equals(safeEmail)) {
                return new ValidatorResult.Error(
                        new DeferredText.Resource(R.string.sign_up_page_label_error_account_with_this_email_already_exists),
                        ErrorType.ACCOUNT_WITH_THIS_EMAIL_ALREADY_EXISTS
                );
            }
        }

        return new ValidatorResult.Success();
    }
}