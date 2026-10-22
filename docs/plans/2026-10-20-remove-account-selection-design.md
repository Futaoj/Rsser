# Remove account selection from onboarding

## Approved behavior

- Keep the existing welcome screen for new users.
- Its Get Started action opens Discover directly, with onboarding mode enabled.
- Completing Discover marks onboarding complete and opens Home. Returning users continue to open Home at startup.
- Remove the account selection screen, its navigation route, and its localized strings.
- Keep cloud sync implementations and provider login flows that remain accessible from Settings.

## Scope

Change the onboarding effect and navigation wiring, remove the account-selection screen and supporting files, remove the `Screen.AccountSelection` route, and delete its resource strings in all locales. Preserve the existing onboarding completion preference and startup routing in `PlaceholderViewModel`.

## Verification

Compile the Android debug variant and confirm there are no remaining account-selection references in source resources or navigation.
