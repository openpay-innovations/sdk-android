# Contributing to the Openpay SDK for Android

## Bug Reports, Feature Requests and Questions

Before submitting a new GitHub issue, please make sure to:

- Review the [README][readme] and other documentation in this repository.
- Search [existing issues][issues].

If the above doesn't help, please [submit an issue][new-issue] via GitHub.

## Contributing Code

### Checking out the Code

- [Fork][fork-repo] the repository.
- Clone your fork:

    `git clone git@github.com:<GITHUB_USER>/sdk-android.git`
- See the [GitHub documentation][fork-docs] on working with forks.

#### Recommended

- Create a new branch to work on with `git checkout -b <BRANCH_NAME>`.
    - Branch names should be descriptive; e.g. `docs/update-contribution-guide`, `fix/checkout-crash`.
- Write [good commit messages][commit-messages].

## Testing

- Please write unit tests for your code changes.
- Before submitting your pull request, run the unit tests in Android Studio or from the console with `./gradlew test`.

## Submitting a Pull Request

Everything you need to know about submitting a PR is contained in our [Pull Request Template][pr-template], but some best practices are:

- Use a descriptive title.
- Add a concise summary of the changes.
- Link to related issues.

## Undergoing Review

During the course of your PR review you may be asked to address comments and make changes before it is accepted and merged. Please refrain from amending commits or force pushing to your branch as making each change as a new commit helps those reviewing your code to better understand exactly what has changed since their last review.

When you have addressed all comments please alert the reviewer with a comment or via GitHub's request review option. See [GitHub's documentation for dealing with Pull Requests][pr-docs].

Your submission will not be immediately available to all users after merging but will ship as part of the next release.

## Code of Conduct

Help us keep this project diverse, open and inclusive. Please read and follow our [Code of Conduct][code-of-conduct].

## Thanks for Contributing!

Thank you for taking the time to contribute to the project!

## License

This project is subject to the conditions detailed in the repository's [LICENSE][license]. All contributions to this project are subject to the same license as per [GitHub's Terms of Service][github-terms-contribution].

<!-- Links: -->
[code-of-conduct]: CODE_OF_CONDUCT.md
[commit-messages]: https://chris.beams.io/posts/git-commit/
[fork-repo]: https://github.com/openpay-innovations/sdk-android/fork
[fork-docs]: https://help.github.com/articles/working-with-forks/
[github-terms-contribution]: https://help.github.com/en/github/site-policy/github-terms-of-service#6-contributions-under-repository-license
[issues]: https://github.com/openpay-innovations/sdk-android/issues
[license]: LICENSE
[new-issue]: https://github.com/openpay-innovations/sdk-android/issues/new/choose
[pr-template]: .github/pull_request_template.md
[pr-docs]: https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/requesting-a-pull-request-review
[readme]: README.md
