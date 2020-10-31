#ifndef FOCUSER_CONFIG_H
#define FOCUSER_CONFIG_H

// Status LED pin
#define LED1 12 //16 //13
//#define LED2 17
#define BLINK_PERIOD 300

// ----- Stepper driver configuration -----
// Microstepping pin mapping: none (0), DRV8825 (1), A4988 (2), ULN2003 (3)
#define STEPPER_TYPE 1

#if STEPPER_TYPE == 3
// Driver pins
#define DRIVER_IN1 5
#define DRIVER_IN2 4
#define DRIVER_IN3 3
#define DRIVER_IN4 2
// Speeds & steps
#define STEPS_REV 4076 // Actual 4076
#define MOTOR_ACCEL 80
#define MOTOR_RPM_MAX 100
#define MOTOR_RPM_MIN 40
// The time (in milliseconds) to wait before turning off the motor
// if no movements are being done and the hold control is enabled
#define DRIVER_POWER_TIMEOUT 2000
#define DEFAULT_ENABLE_HOLD_CONTROL true
#else
// Driver pins
#define DRIVER_STEP 3
#define DRIVER_DIR 2
#define DRIVER_EN 7
#define MODE0 6
#define MODE1 5
#define MODE2 4
// Speeds & steps
#define STEPS_REV 200.0
#define MOTOR_ACCEL 10.0
#define MOTOR_RPM_MAX 500.0
#define MOTOR_RPM_MIN 150.0
// The time (in milliseconds) to wait before turning off the motor
// if no movements are being done and the hold control is enabled
#define DRIVER_POWER_TIMEOUT 30000
#define DEFAULT_ENABLE_HOLD_CONTROL false
#endif

#endif