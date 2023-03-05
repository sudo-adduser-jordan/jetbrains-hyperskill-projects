# tests for the string_to_lower() function
import unittest


class TestStringToLower(unittest.TestCase):
    def test_string_to_lower(self):
        # testing for an exception one way
        self.assertRaises(ValueError, string_to_lower, 1)

        # testing for an exception another way
        with self.assertRaises(ValueError):
            string_to_lower(1)


if __name__ == '__main__':
    unittest.main()