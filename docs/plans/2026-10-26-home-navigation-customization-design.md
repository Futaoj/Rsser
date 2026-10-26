# Home Navigation Customization Design

## Goal

Allow users to choose and order the four phone bottom-navigation entries from the six existing main destinations plus AI chat, and add an AI chat entry to the expanded and collapsed side navigation.

## Approved behavior

- The candidate list contains Home, Subscriptions/Sources, Search, Discovery, Bookmarks, Settings, and AI chat.
- Exactly four distinct candidates are shown in the phone bottom bar.
- The user chooses each slot and can change its position from Settings.
- The default order remains Home, Subscriptions, Discovery, Bookmarks.
- Settings are persisted in the existing Preferences DataStore and observed so the bottom bar updates immediately.
- AI chat is always available in the side navigation, independently of whether it is selected for the bottom bar.
- Selecting AI chat navigates to the existing chat screen; existing API-key and conversation behavior remains unchanged.

## Architecture

Store the ordered selection as a validated preference through `SettingsRepository`. Expose it through the existing app/settings state flows. Render four selection controls in a dedicated settings page and build the phone navigation bar from the persisted selection. Keep the current six main destinations and add AI as a separate action because its route is owned by the outer navigator rather than the nested main-navigation stack.

## Edge cases

- Missing, malformed, duplicate, or incomplete saved values fall back to the current four defaults.
- Each selection control excludes choices already used in the other slots, preserving four unique entries.
- Desktop side navigation continues to show its existing destinations and gains an AI action; the phone bottom bar remains four items.

## Validation

Compile the FOSS debug Android variant and inspect the resulting code diff. Automated tests are not included in this change.
