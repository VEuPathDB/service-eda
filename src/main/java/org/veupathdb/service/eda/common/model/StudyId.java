package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Parses and represents a study identifier that may be either a plain study id
 * ("studyA") or a union meta-study token ("union(studyA,studyB,studyC)").
 *
 * Member ids are alphanumeric/underscore. The union(...) wrapper is explicit so a
 * bare id can never be mistaken for a union. Reserves room for a future
 * extension(...) type by keeping members as an ordered list.
 */
public final class StudyId {

  private static final String UNION_PREFIX = "union(";
  private static final String UNION_SUFFIX = ")";
  private static final Pattern MEMBER_ID = Pattern.compile("[A-Za-z0-9_]+");

  private final String _rawToken;
  private final boolean _isUnion;
  private final List<String> _members;

  private StudyId(String rawToken, boolean isUnion, List<String> members) {
    _rawToken = rawToken;
    _isUnion = isUnion;
    _members = List.copyOf(members);
  }

  /** Bypass parsing for an id already known to be a plain (non-union) study id. */
  public static StudyId forSingle(String id) {
    return new StudyId(id, false, List.of(id));
  }

  public static StudyId parse(String raw) {
    if (raw == null || raw.isBlank())
      throw new BadRequestException("studyId must not be blank");
    String token = raw.trim();

    if (!token.startsWith(UNION_PREFIX)) {
      // plain id path: identity pass-through — preserve the ORIGINAL raw string byte-for-byte
      // (not the trimmed value) so single-study resolution/permission is exactly as before.
      return new StudyId(raw, false, List.of(raw));
    }

    if (!token.endsWith(UNION_SUFFIX))
      throw new BadRequestException("Malformed union token (missing closing paren): " + raw);

    String inner = token.substring(UNION_PREFIX.length(), token.length() - UNION_SUFFIX.length());
    // any further '(' or ')' inside is trailing junk / nesting → reject
    if (inner.contains("(") || inner.contains(")"))
      throw new BadRequestException("Malformed union token (nested or unbalanced parens): " + raw);

    // union() or union(   ) — no members at all
    if (inner.isBlank())
      throw new BadRequestException("Malformed union token (empty member list): " + raw);

    List<String> members = new ArrayList<>();
    LinkedHashSet<String> seen = new LinkedHashSet<>();
    for (String part : inner.split(",", -1)) {
      String member = part.trim();
      if (member.isEmpty())
        throw new BadRequestException("Malformed union token (empty member): " + raw);
      if (!MEMBER_ID.matcher(member).matches())
        throw new BadRequestException("Malformed union token (illegal member id '" + member + "'): " + raw);
      if (!seen.add(member))
        throw new BadRequestException("Malformed union token (duplicate member '" + member + "'): " + raw);
      members.add(member);
    }
    if (members.size() < 2)
      throw new BadRequestException("A union must have at least two members (use the plain id instead): " + raw);

    return new StudyId(token, true, members);
  }

  public boolean isUnion() { return _isUnion; }

  public List<String> getMembers() { return _members; }

  public String getRawToken() { return _rawToken; }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StudyId other)) return false;
    return _rawToken.equals(other._rawToken);
  }

  @Override public int hashCode() { return Objects.hash(_rawToken); }

  @Override public String toString() { return _rawToken; }
}
