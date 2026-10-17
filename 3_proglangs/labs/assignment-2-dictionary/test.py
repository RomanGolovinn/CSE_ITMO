import subprocess
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parent
PROGRAM = ROOT / "main"


def run_program(input_text):
    return subprocess.run(
        [str(PROGRAM)],
        input=input_text.encode(),
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        check=False,
    )


class DictionaryTests(unittest.TestCase):
    def test_each_dictionary_entry(self):
        cases = {
            "third word": "third word explanation\n",
            "second word": "second word explanation\n",
            "first word": "first word explanation\n",
        }

        for key, expected_output in cases.items():
            with self.subTest(key=key):
                result = run_program(key + "\n")
                self.assertEqual(result.returncode, 0)
                self.assertEqual(result.stdout.decode(), expected_output)
                self.assertEqual(result.stderr, b"")

    def test_unknown_key_is_reported_on_stderr(self):
        result = run_program("unknown word\n")

        self.assertEqual(result.returncode, 1)
        self.assertEqual(result.stdout, b"")
        self.assertEqual(result.stderr.decode(), "Word not found.\n")

    def test_empty_input_is_reported_on_stderr(self):
        result = run_program("")

        self.assertEqual(result.returncode, 1)
        self.assertEqual(result.stdout, b"")
        self.assertEqual(
            result.stderr.decode(), "Input is empty or the word is too long.\n"
        )

    def test_input_longer_than_255_characters_is_rejected(self):
        result = run_program("x" * 256 + "\n")

        self.assertEqual(result.returncode, 1)
        self.assertEqual(result.stdout, b"")
        self.assertEqual(
            result.stderr.decode(), "Input is empty or the word is too long.\n"
        )

    def test_255_character_input_fits_in_buffer(self):
        result = run_program("x" * 255 + "\n")

        self.assertEqual(result.returncode, 1)
        self.assertEqual(result.stderr.decode(), "Word not found.\n")

    def test_key_without_trailing_newline(self):
        result = run_program("third word")

        self.assertEqual(result.returncode, 0)
        self.assertEqual(result.stdout.decode(), "third word explanation\n")
        self.assertEqual(result.stderr, b"")


if __name__ == "__main__":
    unittest.main(verbosity=2)
