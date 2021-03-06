package uk.co.desirableobjects.extravalidators

import org.codehaus.groovy.grails.validation.AbstractConstraint
import org.springframework.validation.Errors

class ConfirmedPasswordConstraint extends AbstractConstraint {

    public static final String VALIDATION_DSL_NAME = 'confirmedPassword'
    public static final String DEFAULT_NOT_PASSWORD_CONFIRMATION_MESSAGE_CODE = 'default.not.confirmedPassword.message'

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        String confirmationValue = target."${constraintParameter}"

        if (!validatePasswordConfirmation(target, confirmationValue, propertyValue)) {
            Object[] args = [constraintPropertyName, constraintOwningClass, confirmationValue]
            super.rejectValue(target, errors, DEFAULT_NOT_PASSWORD_CONFIRMATION_MESSAGE_CODE,
                "not.${VALIDATION_DSL_NAME}", args)
        }
    }

    @Override
    public void setParameter(Object constraintParameter) {
        if(constraintOwningClass.metaClass.hasMetaProperty(constraintParameter) && constraintParameter instanceof String) {
            super.setParameter(constraintParameter)
        } else {
            throw new IllegalArgumentException("""Parameter for constraint [${VALIDATION_DSL_NAME}] of property
                [${constraintParameter}] must be the name of a field on class [${constraintOwningClass}]""")
        }
    }

    boolean validatePasswordConfirmation(target, confirmationValue, propertyValue) {
        return confirmationValue == propertyValue
    }

    @Override
    boolean supports(Class type) {
        return type != null && String.class.isAssignableFrom(type)
    }

    @Override
    String getName() {
        return VALIDATION_DSL_NAME
    }
}
