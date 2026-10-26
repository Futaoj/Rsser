# DeepSeek RSS Chat Design

## Goal

Replace the home screen's pinned-sources bottom bar with an AI entry that opens a Chinese chat screen backed by DeepSeek and grounded in the user's recent locally stored RSS article titles and summaries.

## Approved approach

Use direct Android client calls to DeepSeek's OpenAI-compatible chat completions API. The user supplies their own API key in the AI screen; store it locally using Android Keystore-backed encryption. Do not add a backend or embed a key in the app. DeepSeek's documented endpoint is `POST https://api.deepseek.com/chat/completions` and responses contain assistant text in the first choice's message content.

## User experience

- Replace the home pinned-sources bottom bar and its empty-state message with a persistent AI chat entry in the same bottom position.
- Remove the setting that toggles the pinned-sources bottom bar because the bar no longer exists. Preserve pin/unpin actions in the source drawer/list.
- Keep the existing now-playing controls available in the bottom region when audio is active.
- The AI destination has a message list, composer, send action, loading state, clear error messages, and a settings affordance for the DeepSeek API key.
- If the user has not configured a key, explain how to set it before sending.
- Before the first request, clearly tell the user that recent RSS article titles and summaries are sent to DeepSeek. The user has already chosen recent titles and summaries as the context scope.

## Data and request flow

1. The AI ViewModel requests a bounded list of recent local posts from the data layer, ordered newest first, and resolves each post's feed name and summary.
2. Context is capped by article count and per-article text length so a large local database cannot create an unbounded request. The prompt includes title, feed name, date, summary, and link for source references.
3. The client sends a system message containing this context plus the current conversation messages to the DeepSeek chat completions endpoint. No full article body or unrelated user data is sent.
4. Responses are rendered as assistant messages. Network, authentication, rate-limit, malformed-response, and empty-feed errors are surfaced in Chinese.
5. The API key is encrypted at rest using Android Keystore-backed encryption. Conversation history remains in memory for the current screen session in this first version.

## Components

- `core/data`: add a focused recent-post query and repository method joining posts to feeds.
- `feature/reader`: add DeepSeek request/response models, client, ViewModel/state/events, chat screen, and localized strings.
- Navigation: add an AI route and use the home bottom bar callback to open it.
- Settings: remove the obsolete pinned-bottom-bar preference control and state wiring.

## Alternatives considered

1. **Direct client with user-provided key (chosen):** works with the current app architecture and needs no server; users must provide a key and article summaries are sent to DeepSeek.
2. **Backend proxy:** keeps provider credentials off-device but requires hosting, authentication, and ongoing server cost that this project does not currently have.
3. **Manual per-article context:** minimizes data transfer but does not satisfy the request for the AI to read the subscribed RSS sources as a whole.

## Verification

- Compile the Android `fullDebug` variant.
- Verify navigation opens the AI screen from the former bottom-bar position and returns home correctly.
- Verify no-key, empty-feed, HTTP error, and successful response states are handled.
- Ensure the API key is not logged or stored in plaintext and that request context only contains bounded recent titles and summaries.

## Constraints

- Project root: `/Users/wanghan/Projects/git_clone/rsser_a`.
- This directory is not a Git repository, so the design cannot be committed here.
- Current project localization is Simplified Chinese; new user-facing strings should be Chinese.
